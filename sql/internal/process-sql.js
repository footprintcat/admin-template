const fs = require('fs');
const path = require('path');

// usage: node process-sql.js <input.sql> <output.sql> <order.json>

/*
cd sql/internal
node process-sql.js ./admin_template.sql ../database-init.sql ./order.json
*/
// 或者使用默认参数 （见 `main()` 函数）
// node process-sql.js

/**
 * 处理 SQL 文件
 *
 * @param {boolean} isDevelopSQL - 是否为开发环境 SQL
 * @param {string} inputFilePath - 输入 SQL 文件路径
 * @param {string} outputFilePath - 输出 SQL 文件路径
 * @param {Array|string} tableOrder - 表名排序顺序（可以是数组或JSON文件路径）
 */
function processSQLFile(isDevelopSQL, inputFilePath, outputFilePath, tableOrder = []) {

  // 是否跳过 DROP TABLE IF EXISTS 语句
  const skipDropTableIfExists = !isDevelopSQL

  // 是否跳过 @xxx@ 特殊表
  const skipSpecialTables = !isDevelopSQL

  try {
    // 读取 SQL 文件
    const sqlContent = fs.readFileSync(inputFilePath, 'utf8');

    // 标准化换行符为 \n，便于处理
    const normalizedContent = sqlContent.replace(/\r\n/g, '\n');

    // 找到 SET FOREIGN_KEY_CHECKS = 0; 和 SET FOREIGN_KEY_CHECKS = 1; 之间的内容
    const startMarker = 'SET FOREIGN_KEY_CHECKS = 0;';
    const endMarker = 'SET FOREIGN_KEY_CHECKS = 1;';

    const startIndex = normalizedContent.indexOf(startMarker);
    const endIndex = normalizedContent.indexOf(endMarker);

    if (startIndex === -1 || endIndex === -1) {
      console.error('未找到 FOREIGN_KEY_CHECKS 标记');
      return;
    }

    // 提取中间部分
    const middleContent = normalizedContent.substring(startIndex + startMarker.length, endIndex).trim();

    // 按双换行分割成块
    const blocks = middleContent.split('\n\n').filter(block => block.trim());

    // 处理每个块
    const tableBlocks = [];

    for (const block of blocks) {
      const blockStr = block.trim();
      if (!blockStr) continue;

      // 如果是表结构块
      if (blockStr.includes('Table structure for')) {
        const lines = blockStr.split('\n');
        const processedLines = [];

        let i = 0;
        while (i < lines.length) {
          const line = lines[i].trim();

          // 跳过 DROP TABLE IF EXISTS 行
          if (skipDropTableIfExists) {
            if (line.includes('DROP TABLE IF EXISTS')) {
              i++;
              continue;
            }
          }

          // 如果是 CREATE TABLE 语句，移除 AUTO_INCREMENT
          if (line.includes('CREATE TABLE')) {
            // 收集完整的 CREATE TABLE 语句
            let createStmt = lines[i]; // 保持原始格式（可能有末尾空格）

            // 向后找，直到找到分号或到达行尾
            let foundSemicolon = line.includes(';');
            let j = i + 1;

            while (j < lines.length && !foundSemicolon) {
              createStmt += '\n' + lines[j];
              foundSemicolon = lines[j].includes(';');
              j++;
            }

            // 如果找到了完整的语句
            if (foundSemicolon) {
              i = j; // 跳过已处理的行

              // 移除 AUTO_INCREMENT
              createStmt = createStmt.replace(/ AUTO_INCREMENT\s*=\s*\d+/gi, '');
              createStmt = createStmt.replace(/\s+AUTO_INCREMENT\b/gi, '');

              processedLines.push(createStmt);
            } else {
              // 如果没有找到分号，保持原样
              processedLines.push(lines[i]);
              i++;
            }
          } else {
            // 普通行（注释等）
            processedLines.push(lines[i]);
            i++;
          }
        }

        const processedBlock = processedLines.join('\n').trim();
        if (processedBlock) {
          const tableName = extractTableName(processedBlock);
          if (tableName) {
            if (skipSpecialTables && tableName.startsWith('@') && tableName.endsWith('@')) {
              console.log(' ', `跳过特殊表: ${tableName}`)
              continue
            }
            console.log(' ', `表: ${tableName}`)
            tableBlocks.push({
              block: processedBlock,
              tableName: tableName
            });
          }
        }
      }
    }

    console.log(`找到表块: ${tableBlocks.map(t => t.tableName).join(', ')}`);

    // 处理 tableOrder 参数：可能是数组，也可能是 JSON 文件路径
    let orderArray = [];
    if (typeof tableOrder === 'string') {
      // 如果是字符串，尝试作为文件路径
      if (fs.existsSync(tableOrder)) {
        try {
          const orderData = JSON.parse(fs.readFileSync(tableOrder, 'utf8'));
          if (Array.isArray(orderData)) {
            orderArray = orderData;
            console.log(`从文件加载排序顺序: ${tableOrder}`);
          } else if (orderData.tables && Array.isArray(orderData.tables)) {
            orderArray = orderData.tables;
            console.log(`从文件加载排序顺序: ${tableOrder}`);
          } else {
            console.warn(`JSON文件格式不正确，期待数组或包含tables字段的对象: ${tableOrder}`);
            orderArray = tableOrder.split(','); // 回退到逗号分割
          }
        } catch (jsonError) {
          console.warn(`无法解析JSON文件 ${tableOrder}:`, jsonError.message);
          orderArray = tableOrder.split(','); // 回退到逗号分割
        }
      } else {
        // 不是文件路径，当作逗号分割的字符串
        orderArray = tableOrder.split(',');
      }
    } else if (Array.isArray(tableOrder)) {
      orderArray = tableOrder;
    }

    // 过滤空值并打印
    orderArray = orderArray.filter(name => name && name.trim()).map(name => name.trim());
    console.log(`排序顺序: ${orderArray.join(', ')}`);

    // 按表名排序（如果有指定顺序）
    if (orderArray.length > 0) {
      tableBlocks.sort((a, b) => {
        const indexA = orderArray.indexOf(a.tableName);
        const indexB = orderArray.indexOf(b.tableName);

        if (indexA === -1 && indexB === -1) return 0;
        if (indexA === -1) return 1;
        if (indexB === -1) return -1;

        return indexA - indexB;
      });

      console.log(`排序后: ${tableBlocks.map(t => t.tableName).join(', ')}`);
    }

    // 组合最终的 SQL
    const finalSQL = tableBlocks.map(item => item.block).join('\n\n') + '\n';

    const finalSQL2 = finalSQL
      .replaceAll(') ENGINE = InnoDB', ')\nENGINE = InnoDB') // 换行
      // 优化 COLLATE 和 COMMENT 之间的换行
      .replaceAll('COLLATE = utf8mb4_0900_ai_ci COMMENT', 'COLLATE = utf8mb4_0900_ai_ci\nCOMMENT') // 换行
      .replaceAll('ROW_FORMAT = DYNAMIC', 'ROW_FORMAT = Dynamic') // 统一为大驼峰
      .replaceAll(' ROW_FORMAT = Dynamic', '\nROW_FORMAT = Dynamic') // 换行
      .replaceAll('`  (', '` (') // 去除多余空格

    const initialDataSQL = // 带前后换行
      '\n' +
      fs.readFileSync(path.join(path.dirname(inputFilePath), 'initial_data.sql'), 'utf8').trim() +
      '\n' +
      (
        isDevelopSQL
          ? '\n' + fs.readFileSync(path.join(path.dirname(inputFilePath), 'initial_data_develop.sql'), 'utf8').trim() + '\n'
          : ''
      )

    const finalFileContent =
      '-- ----------------------------\n' +
      '-- 数据库初始设置文件\n' +
      '-- ----------------------------\n' +
      '-- 包含：\n' +
      '--   1. 所有表结构定义\n' +
      '--   2. 必需的基础数据\n' +
      '--\n' +
      '-- 注意：\n' +
      '--   1. 本 sql 文件由 internal 目录中脚本生成，重新生成会被覆盖，不建议直接修改\n' +
      '--   2. 请先创建数据库，并使用 `use <database>;` 命令进入对应数据库后再执行当前脚本\n' +
      '--   3. 执行此文件会创建所有表并插入初始数据\n' +
      '--   4. ⚠ 请勿重复执行本脚本 ⚠\n' +
      '-- ----------------------------\n' +
      '\n' +
      'SET NAMES utf8mb4;\n' +
      `${startMarker}\n` +
      '\n' +
      '-- ----------------------------\n' +
      '-- 基础表结构定义\n' +
      '-- ----------------------------\n' +
      '\n' +
      finalSQL2 +
      initialDataSQL +
      '\n' +
      endMarker +
      '\n';

    if (finalFileContent.includes('\r\n')) {
      console.warn('⚠ 文件中含有 CRLF 换行符！')
    }

    // 保存到新文件
    fs.writeFileSync(outputFilePath, finalFileContent, 'utf8');

    console.log(`\n处理完成！输出文件已保存至: ${outputFilePath}`);
    console.log(`处理了 ${tableBlocks.length} 个表结构`);

  } catch (error) {
    console.error('处理 SQL 文件时出错:', error.message);
    console.error(error.stack);
  }
}

/**
 * 从表块中提取表名
 */
function extractTableName(tableBlock) {
  // 尝试多种模式匹配表名
  const patterns = [
    /Table structure for [`"]?([^`"\s]+)[`"]?/,  // Table structure for `table_name`
    /CREATE TABLE [`"]?([^`"\s]+)[`"]?\s*\(/     // CREATE TABLE `table_name` (
  ];

  for (const pattern of patterns) {
    const match = tableBlock.match(pattern);
    if (match) {
      return match[1];
    }
  }

  return null;
}

/**
 * 主函数
 */
function main() {
  const config = {
    inputFile: './admin_template.sql',
    outputFile: '../database-init.sql',
    tableOrder: 'order.json', // [] // 默认顺序
  };

  if (!fs.existsSync(config.inputFile)) {
    console.error(`输入文件不存在: ${config.inputFile}`);
    console.log('用法:');
    console.log('1. node process-sql.js');
    console.log('2. node process-sql.js input.sql output.sql');
    console.log('3. node process-sql.js input.sql output.sql order.json');
    console.log('4. node process-sql.js input.sql output.sql table1,table2,table3');
    return;
  }

  console.log('开始处理 SQL 文件...');
  console.log('输入文件:', config.inputFile);
  console.log('输出文件:', config.outputFile);
  console.log('表排序顺序:', config.tableOrder);
  console.log('---');

  processSQLFile(false, config.inputFile, config.outputFile, config.tableOrder);
  processSQLFile(true, config.inputFile, config.outputFile.replace('.sql', '.develop.sql'), config.tableOrder);
}

/**
 * 命令行运行
 */
function runStandalone() {
  const args = process.argv.slice(2);

  if (args.length === 0) {
    // 无参数：使用默认配置
    main();
  } else if (args.length >= 2) {
    const inputFile = args[0];
    const outputFile = args[1];
    let tableOrderParam = args[2] || []; // 第三个参数可选

    console.log('使用命令行参数:');
    console.log('输入文件:', inputFile);
    console.log('输出文件:', outputFile);
    console.log('表顺序参数:', tableOrderParam || '(无)');
    console.log('---');

    // 如果提供了第三个参数，当作排序依据
    processSQLFile(false, inputFile, outputFile, tableOrderParam);
    processSQLFile(true, inputFile, outputFile.replace('.sql', '.develop.sql'), tableOrderParam);
  } else {
    console.log('用法:');
    console.log('1. node process-sql.js');
    console.log('2. node process-sql.js input.sql output.sql');
    console.log('3. node process-sql.js input.sql output.sql order.json');
    console.log('4. node process-sql.js input.sql output.sql table1,table2,table3');
    console.log('');
    console.log('示例:');
    console.log('node process-sql.js admin_template.sql output.sql');
    console.log('node process-sql.js admin_template.sql output.sql order.json');
    console.log('node process-sql.js admin_template.sql output.sql system_user,role,system_tenant');
  }
}

// 运行脚本
runStandalone();

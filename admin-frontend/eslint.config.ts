/* eslint-disable import/order */
import { globalIgnores } from 'eslint/config'
import { defineConfigWithVueTs, vueTsConfigs } from '@vue/eslint-config-typescript'
import pluginVue from 'eslint-plugin-vue'
import skipFormatting from '@vue/eslint-config-prettier/skip-formatting'
import eslintPluginImport from 'eslint-plugin-import'

// To allow more languages other than `ts` in `.vue` files, uncomment the following lines:
// import { configureVueProject } from '@vue/eslint-config-typescript'
// configureVueProject({ scriptLangs: ['ts', 'tsx'] })
// More info at https://github.com/vuejs/eslint-config-typescript/#advanced-setup

export default defineConfigWithVueTs(
  {
    name: 'app/files-to-lint',
    files: ['**/*.{ts,mts,tsx,vue}'],
  },

  globalIgnores(['**/dist/**', '**/dist-ssr/**', '**/coverage/**']),

  pluginVue.configs['flat/essential'],
  vueTsConfigs.recommended,
  skipFormatting,

  {
    // 自定义规则
    rules: {
      // 禁止使用行末分号
      semi: ['error', 'never'],
      // 强制使用单引号
      'quotes': ['error', 'single'],
      // 已声明但从未读取其值 调整为 warn 而非 error
      '@typescript-eslint/no-unused-vars': ['warn',
        {
          'args': 'none',        // 不检查函数参数
          'vars': 'all',         // 检查所有变量
          'varsIgnorePattern': '^_', // 忽略以_开头的变量
          'ignoreRestSiblings': true, // 忽略解构剩余部分
        }],
      // 强制使用尾随逗号
      'comma-dangle': ['error', 'always-multiline'],
    },
  },
  {
  // import 自动排序
    plugins: {
      import: eslintPluginImport,
    },
    rules: {
      // Import 排序规则
      'import/order': [
        'error',
        {
          groups: [
            'builtin', // Node.js 内置模块
            'external', // 第三方库
            'internal', // 内部模块（别名路径）
            'parent',   // 父级目录导入
            'sibling', // 同级目录导入
            'index',    // 当前目录索引文件
            'object',   // 对象导入
          ],
          'newlines-between': 'always', // 组之间用空行分隔
          alphabetize: {
            order: 'asc', // 按字母顺序升序排列
            caseInsensitive: true, // 忽略大小写
          },
          pathGroups: [
            {
              pattern: '@/**', // 如果你的项目有 @ 别名
              group: 'internal',
              position: 'before',
            },
            {
              pattern: '~/**', // 如果有 ~ 别名
              group: 'internal',
              position: 'before',
            },
          ],
          pathGroupsExcludedImportTypes: ['builtin'],
        },
      ],
      // 确保所有导入都在文件顶部
      'import/first': 'error',
      // 导入后需要空行
      'import/newline-after-import': 'error',
      // 禁止重复导入
      'import/no-duplicates': 'error',
      // 禁止使用默认导出作为命名导入
      'import/no-named-as-default': 'warn',
      // 禁止未使用的导入
      'import/no-unused-modules': 'warn',

      'import/exports-last': 'error', // 导出放在文件末尾
      'import/no-absolute-path': 'error', // 禁止绝对路径导入
      'import/no-webpack-loader-syntax': 'error', // 禁止 webpack loader 语法
    },
  },
)

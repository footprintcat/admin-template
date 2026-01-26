/* eslint-disable import/order */
import { globalIgnores } from 'eslint/config'
import { defineConfigWithVueTs, vueTsConfigs } from '@vue/eslint-config-typescript'
import pluginVue from 'eslint-plugin-vue'
import pluginOxlint from 'eslint-plugin-oxlint'
import skipFormatting from 'eslint-config-prettier/flat'
import eslintPluginImport from 'eslint-plugin-import'

// To allow more languages other than `ts` in `.vue` files, uncomment the following lines:
// import { configureVueProject } from '@vue/eslint-config-typescript'
// configureVueProject({ scriptLangs: ['ts', 'tsx'] })
// More info at https://github.com/vuejs/eslint-config-typescript/#advanced-setup

export default defineConfigWithVueTs(
  {
    name: 'app/files-to-lint',
    files: ['**/*.{vue,ts,mts,tsx}'],
  },

  globalIgnores(['**/dist/**', '**/dist-ssr/**', '**/coverage/**']),

  ...pluginVue.configs['flat/essential'],
  vueTsConfigs.recommended,

  ...pluginOxlint.buildFromOxlintConfigFile('.oxlintrc.json'),

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
            'type',  // import type 分组
          ],
          // // 'newlines-between': 'always', // 组之间用空行分隔
          'newlines-between': 'never', // 组之间不使用空行分隔
          alphabetize: {
            order: 'asc', // 按字母顺序升序排列
            caseInsensitive: true, // 忽略大小写
          },
          pathGroups: [
            // 添加 Vue 相关导入的路径组，确保它们在最前面
            ...[
              'vue',
              'vue-router',
              'pinia',
              'element-plus',
              '@element-plus/**',
            ].map(pattern => ({
              pattern: pattern,
              group: 'external',
              position: 'before',
            })),
            {
              pattern: '@/utils/**',
              group: 'internal',
              position: 'before',
            },
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
            // 将类型导入单独分组
            {
              pattern: '**/*.types',
              group: 'internal',
              position: 'after',
            },
            {
              pattern: '**/types',
              group: 'internal',
              position: 'after',
            },
          ],
          // 区分类型导入和值导入
          // distinctGroup: false,
          distinctGroup: true,  // 改为 true 以区分类型导入
          // 类型导入放在值导入之后
          warnOnUnassignedImports: true,
          pathGroupsExcludedImportTypes: ['builtin'],
        },
      ],

      // 强制类型导入使用 import type
      '@typescript-eslint/consistent-type-imports': [
        'error',
        {
          prefer: 'type-imports', // 优先使用 type imports
          fixStyle: 'separate-type-imports', // 将类型导入分离到单独的 import 语句
        },
      ],

      // 禁止在类型导入中混合值导入
      '@typescript-eslint/no-import-type-side-effects': 'error',

      // Vue 组合式 API 的导入顺序规则
      'vue/prefer-import-from-vue': 'error', // 优先从 'vue' 导入

      // 自定义规则：确保 Vue API 按特定顺序导入
      'sort-imports': [
        'error',
        {
          ignoreCase: true,
          ignoreDeclarationSort: true, // 让 import/order 处理主要排序
          ignoreMemberSort: false, // 确保成员排序
          memberSyntaxSortOrder: ['none', 'all', 'multiple', 'single'],
          // 自定义成员排序（针对 Vue API）
          allowSeparatedGroups: true,
        },
      ],

      // 确保所有导入都在文件顶部
      'import/first': 'error',
      // 导入后需要空行
      // 'import/newline-after-import': 'error',
      // 确保 import 语句块之后至少有一个空行
      'import/newline-after-import': ['error', { count: 1 }],
      // 禁止重复导入
      // 'import/no-duplicates': 'error',
      'import/no-duplicates': [
        'error',
        {
          considerQueryString: true,
        },
      ],
      // 禁止使用默认导出作为命名导入
      'import/no-named-as-default': 'warn',
      // 禁止未使用的导入
      'import/no-unused-modules': 'warn',

      // 'import/exports-last': 'error', // 导出放在文件末尾
      'import/no-absolute-path': 'error', // 禁止绝对路径导入
      'import/no-webpack-loader-syntax': 'error', // 禁止 webpack loader 语法

      // 不允许导入多次
      // docs: https://eslint.org/docs/latest/rules/no-duplicate-imports
      // 'no-duplicate-imports': 'error',

    },
  },
)

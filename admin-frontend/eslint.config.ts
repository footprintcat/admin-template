import { globalIgnores } from 'eslint/config'
import { defineConfigWithVueTs, vueTsConfigs } from '@vue/eslint-config-typescript'
import pluginVue from 'eslint-plugin-vue'
import skipFormatting from '@vue/eslint-config-prettier/skip-formatting'

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
)

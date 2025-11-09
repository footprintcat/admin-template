// 是否是通过 npm run dev 方式运行的调试模式
const isDev: boolean = import.meta.env.DEV
// 是否是本机运行
const isLocalhost: boolean = location?.host?.includes('127.0.0.1') || location?.host?.includes('localhost')
// 是否在 [本机] 或 [192.168. 开头的局域网] 内运行
const isLAN: boolean = isLocalhost || location?.host?.startsWith('192.168.')
// 是否是 https 协议
const isHttps: boolean = location.protocol === 'https:'

export default {

  /**
   * 是否是调试模式
   * （仅作业务层面区分，与开发环境 / 生产环境无关
   *   本地调试时可以修改为 true 以观察生产环境表现）
   *
   * true:  开启调试
   * false: 关闭调试
   */
  debugMode: isDev,

  /**
   * 当前是否为局域网环境
   */
  isLAN: isLAN,

  /**
   * 当前是否是 https 协议
   */
  isHttps: isHttps,

  /**
   * 网站名称
   * （网页标题 / 站点信息显示）
   */
  siteTitle: '简构后台管理',

  /**
   * 网站全称
   * （登录页标题 / 左上角标题 / dashboard 标题 / 站点信息显示）
   */
  siteFullTitle: '简构后台管理系统',

  /**
   * 开发公司名称
   * （留空则不显示）
   */
  companyName: '武汉脚印猫科技有限公司',

  /**
   * 开发公司名称展示位置
   */
  companyNameConfig: {
    showInLoginPage: true,
    showInDashboard: true,
  },

  /**
   * 网站备案号
   * （留空则不展示）
   */
  icpNumber: '',

  /**
   * 后端接口请求地址
   *
   * 结尾不能包含 /
   *
   * example:
   * http://127.0.0.1:8080
   * http://localhost:8080
   * http://192.168.2.21:8080
   * https://dev.mine.footprintcat.com/api
   */
  backendHost: isLAN
    ? `${location.protocol}//${location.hostname}:8080`
    : `${location.origin}/api`,

  /**
   * 后端 Websocket 请求地址
   *
   * 结尾不能包含 /
   */
  backendWsHost: isLAN
    ? `ws://${location.hostname}:8080`
    : `${isHttps ? 'wss' : 'ws'}://${location.host}/api`,

}

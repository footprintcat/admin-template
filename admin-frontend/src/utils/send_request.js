// import { clearFrontendLocalStorage } from './local_storage_util'
import request from './request'
import settings from './settings'
import { ElMessage, ElLoading } from 'element-plus'

async function base(url, method = "POST", params, showLoading, callback) {
  if (!url) {
    return false
  }

  const loading = showLoading && ElLoading.service({
    lock: true,
    text: '请稍候',
    background: 'rgba(0, 0, 0, 0.7)',
  });

  // 2024.06.16 修复打开 el-dialog 后, 弹窗在el-loading上层的bug
  const elLaoadingMask = document.querySelector(".el-loading-mask")
  if (elLaoadingMask) {
    elLaoadingMask.style.zIndex = "999999"
  }

  const returnData = await request({
    baseURL: settings.backendHost,
    url: url,
    method: method,
    withCredentials: true,
    // POST 请求参数
    data: method.toUpperCase() == "POST" ? params : null,
    // GET 请求参数
    params: method.toUpperCase() == "GET" ? params : null,
  }).then((response) => {
    let result = response.data
    // 判断后端是否处理成功
    if (!result.isSuccess) {
      // 用户未登录情况
      if (result.data && result.data.errCode == 20003) {
        ElMessage.error({ message: result?.data?.errMsg || "用户未登录", grouping: true });
        // clearFrontendLocalStorage()

        // window.location.reload();
        // 如果同时发出多个请求，可能会多次进来，第二次及之后进入时，hash已经变成 #/login 了
        const locationHash = window.location.hash.substring(1) // 去除最前面的 '#'
        if (!locationHash.includes("/login")) {
          // const newUrl = '/#/login?redirectTo=' + encodeURIComponent(locationHash.split('?')[0])
          const newUrl = '/#/login?redirectTo=' + encodeURIComponent(locationHash)
          console.log("newUrl", newUrl)
          window.location.href = newUrl
          window.location.reload() // 2024.07.10 fix: 修复当url仅有#后面部分改变时，页面不刷新问题
        }
      } else {
        ElMessage.error({ message: result?.message || "服务器错误", grouping: true })
      }
      return false
    }
    let data = result.data
    if (typeof (callback) === "function") {
      let callbackResult = callback(data)
      if (callbackResult !== null && callbackResult !== undefined) {
        return callbackResult
      }
    }
    return true
  }).catch((err) => {
    console.error(err)
    ElMessage.error(err.message)
    // ElMessage.error('请求超时，请检查网络连接')
    return false
  }).finally(() => {
    showLoading && loading.close()
  })
  return returnData
}

async function send_request(url, method = "POST", params, callback) {
  return await base(url, method, params, true, callback)
}
export default send_request
export async function send_request_without_loading(url, method = "POST", params, callback) {
  return await base(url, method, params, false, callback)
}

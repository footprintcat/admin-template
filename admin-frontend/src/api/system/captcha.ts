const API_PREFIX = '/manage/v1/system/captcha'

const baseURL = import.meta.env.VITE_API_BASE_URL

const getUrl = (url: string) => API_PREFIX + url

/**
 * 获取验证码图片
 *
 * @returns 验证码图片的 Blob URL
 */
export function getCaptchaImageUrl() {
  const url = getUrl('/image')
  return baseURL + url + '?t=' + Date.now()
}

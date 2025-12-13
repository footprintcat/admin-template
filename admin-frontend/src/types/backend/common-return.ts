interface BaseCommonReturn<T> {
  status: string
  isSuccess: boolean
  data: T
  message: string | null
}

interface CommonReturnSuccess<T> extends BaseCommonReturn<T> {
  status: 'success'
  isSuccess: true
}

interface CommonReturnFail<T> extends BaseCommonReturn<T> {
  status: 'fail'
  isSuccess: false
}

export type CommonReturn<T> = CommonReturnSuccess<T> | CommonReturnFail<T>

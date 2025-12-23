interface BaseCommonReturn<T> {
  status: string
  isSuccess: boolean
  data: T
  message: string | null
  errCode: number | null
}

interface CommonReturnSuccess<T> extends BaseCommonReturn<T> {
  status: 'success'
  isSuccess: true
  errCode: null
}

interface CommonReturnFail<T> extends BaseCommonReturn<T> {
  status: 'fail'
  isSuccess: false
  errCode: number | null
}

export type CommonReturn<T> = CommonReturnSuccess<T> | CommonReturnFail<T>

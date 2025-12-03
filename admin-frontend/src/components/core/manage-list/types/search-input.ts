type InitialValueType<T> = T | undefined | (() => T)

export interface SearchInputBase {
  /** 搜索项名称 */
  label: string
  /** 查询参数 */
  field: string
  /** 跨几列 - 跨列时指定此项 */
  columnGap?: number
  /** 初值 */
  initialValue?: InitialValueType<unknown>
}

export interface TextSearchInput {
  type: 'text'
  /** 占位符文字 */
  placeHolder?: string
  initialValue?: InitialValueType<string>
}

export interface DropdownSearchInput {
  type: 'dropdown'
  placeHolder?: string
  /** 是否支持多选 */
  multipleSelection?: boolean
  initialValue?: InitialValueType<boolean>
}

export interface DateTimeSearchInput {
  type: 'datetime'
  placeHolder?: string
  initialValue?: InitialValueType<Date>
}

export interface DateTimeRangeSearchInput {
  type: 'datetime-range'
  placeHolderStart?: string
  placeHolderEnd?: string
  initialValue?: InitialValueType<[Date, Date]>
}

export type SearchInput = SearchInputBase &
  (
    | TextSearchInput
    | DropdownSearchInput
    | DateTimeSearchInput
    | DateTimeRangeSearchInput
  )

export type SearchInputList = Array<SearchInput>

// import type { dayjs } from 'element-plus'

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

export interface DateTimeSearchInput<F extends DateTimeFormat> {
  type: 'datetime'
  placeHolder?: string
  valueFormat: F
  /** 是否展示时间部分 */
  showTimePart?: InitialValueType<boolean>
  initialValue?: InitialValueType<DateTimeValueByFormat<F>>
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
    | DateTimeSearchInput<'dateObject'>
    // | DateTimeSearchInput<'dayjsObject'>
    | DateTimeSearchInput<'timestamp'>
    | DateTimeSearchInput<'ISOString'>
    | DateTimeRangeSearchInput
  )

export type SearchInputList = Array<SearchInput>


// 扩展类型

// ==============================
// DateTimeValueFormat
// ==============================

// 映射关系
export type DateTimeFormat =
  | 'dateObject'
  // | 'dayjsObject'
  | 'timestamp'
  | 'ISOString'

// 通过格式映射到值类型
type DateTimeValueByFormat<F extends DateTimeFormat> =
  F extends 'dateObject' ? Date :
  // F extends 'dayjsObject' ? dayjs.Dayjs :
  F extends 'timestamp' ? number :
  F extends 'ISOString' ? string :
  never

// ==============================

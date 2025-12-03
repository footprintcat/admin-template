export interface SearchInputBase {
  /** 搜索项名称 */
  label: string
  /** 跨几列 - 跨列时指定此项 */
  columnGap?: number
}

export interface TextSearchInput {
  type: 'text'
  /** 占位符文字 */
  placeHolder?: string
}

export interface DropdownSearchInput {
  type: 'dropdown'
  placeHolder?: string
  /** 是否支持多选 */
  multipleSelection?: boolean
}

export interface DateTimeSearchInput {
  type: 'datetime'
  placeHolder?: string
}

export interface DateTimeRangeSearchInput {
  type: 'datetime-range'
  placeHolderStart?: string
  placeHolderEnd?: string
}

export type SearchInput = SearchInputBase &
  (
    | TextSearchInput
    | DropdownSearchInput
    | DateTimeSearchInput
    | DateTimeRangeSearchInput
  )

export type SearchInputList = Array<SearchInput>

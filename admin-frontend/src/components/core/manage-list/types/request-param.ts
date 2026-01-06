export interface RequestParam<Param = Record<string, unknown>> {
  params: Param
  sort?: Array<SortItem>
  pageQuery: {
    pageIndex: number
    pageSize: number
  }
}

export interface SortItem {
  field: string
  order: 'ascending' | 'descending' | null
}

export interface SortItemWithLabel extends SortItem {
  label: string // 字段名 仅用于前端展示
}

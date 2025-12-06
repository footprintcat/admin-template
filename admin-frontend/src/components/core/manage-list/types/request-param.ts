export interface RequestParam {
  params: Record<string, unknown>
  sort?: Array<SortItem>
  pageQuery: {
    pageIndex: number,
    pageSize: number,
  },
}

export interface SortItem {
  field: string
  order: 'ascending' | 'descending' | null
}

export interface RequestParam {
  params: Record<string, unknown>
  pageQuery: {
    pageIndex: number,
    pageSize: number,
  },
}

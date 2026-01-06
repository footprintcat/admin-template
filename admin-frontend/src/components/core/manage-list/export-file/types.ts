export interface ExportConfig {
  withFilter: boolean
  ext: string
}

export type HandleExportFunc = (exportInfo: ExportConfig) => void

// 后端返回结构
export interface ExportResult {
  fileName: string // 导出文件名
  head: {
    showHead: boolean
    columns: Array<{
      field: string
      fieldName: string
      columnWidth: number
    }>
  }
  data: Array<{
    [key: string]: string
  }>
}

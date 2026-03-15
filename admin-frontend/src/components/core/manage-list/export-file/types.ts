import type { SupportExportFormats } from './support-export-formats'

export interface ExportConfig {
  dataRange: 'all' | 'current-page' | 'top-n'
  dataRangeTopN: number | null
  filterType: 'none' | 'current'
  sortType: 'none' | 'current'
  ext: SupportExportFormats
  exportType: ComputedRef<ExportConfig_ExportType>
}
export type ExportConfig_ExportType = 'backend' | 'frontend'

export type HandleExportFunc = (exportInfo: ExportConfig) => void

// 后端返回结构
export interface ExportResult {
  fileName: string // 导出文件名
  sheetName: string // 工作表名称
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

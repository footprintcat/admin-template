export const frontendSupportExportFormatTuple =
  ['xlsx', 'xls', 'csv', 'html', 'txt', 'json', 'rtf'] as const

export const backendSupportExportFormatTuple =
  ['xlsx'] as const

export type FrontendExportFormats = typeof frontendSupportExportFormatTuple[number]
export type BackendExportFormats = typeof backendSupportExportFormatTuple[number]
export type SupportExportFormats = FrontendExportFormats | BackendExportFormats

export const frontendSupportExportFormatList: Array<SupportExportFormats> = [...frontendSupportExportFormatTuple]
export const backendSupportExportFormatList: Array<SupportExportFormats> = [...backendSupportExportFormatTuple]
export const possibleSupportExportFormatList: Array<SupportExportFormats> = [
  ...new Set([...frontendSupportExportFormatTuple, ...backendSupportExportFormatTuple]),
]

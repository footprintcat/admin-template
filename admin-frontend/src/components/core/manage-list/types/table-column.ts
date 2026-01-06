export interface TableColumnBase {
  /** 列名称 */
  label: string
  /** 绑定参数 (即 table-column 的 prop 属性) */
  field: string
  /** 列宽 */
  width?: number | string
  minWidth?: number | string
  /** 对齐方式 */
  align?: 'left' | 'center' | 'right'
  /** 表头对齐方式 */
  headerAlign?: 'left' | 'center' | 'right'
  /** 是否固定列 */
  fixed?: 'left' | 'right'
  /** 是否可以排序 */
  sortable: boolean
  /** 默认排序 */
  defaultSort?: 'ascending' | 'descending'
  /** 是否可以调整列宽 */
  resizable?: boolean
  /** 当内容过长被隐藏时显示 tooltip */
  showOverflowTooltip?: boolean
  /** 翻译字典 */
  transformMap?: Record<string | number, string> // 如果继承的 Column 不需要，可以覆盖设置为 never
  transformDefaultValue?: string // 当 transformMap 未命中时的默认显示值. 如果继承的 Column 不需要，可以覆盖设置为 never
}

export interface TextTableColumn extends TableColumnBase {
  type: 'text'
  /** 翻译字典 */
  // transformMap?: Record<string | number, string>
}

export interface DateTimeTableColumn extends TableColumnBase {
  type: 'datetime'
  valueFormat: 'timestamp' | 'ISOString'
  /** 是否展示时间部分 */
  showTimePart?: boolean
  /** 不允许翻译 */
  transformMap?: never
  transformDefaultValue?: never
}

export interface TagTableColumn extends TableColumnBase {
  type: 'tag'
  /** Tag 类型 */
  tagType?: 'success' | 'info' | 'warning' | 'danger' | 'primary'
  /** 翻译字典 */
  // transformMap?: Record<string | number, string>
}

export interface ImageTableColumn extends TableColumnBase {
  type: 'image'
  /** 图片宽度 */
  imageWidth?: number | string
  /** 图片高度 */
  imageHeight?: number | string
  /** 图片不可以排序 */
  sortable: false
  /** 不允许翻译 */
  transformMap?: never
  transformDefaultValue?: never
}

export interface CustomTableColumn extends TableColumnBase {
  type: 'custom'
}

export type TableColumn = TableColumnBase &
  (
    | TextTableColumn
    | DateTimeTableColumn
    | ImageTableColumn
  )

export type TableColumnList = Array<TableColumn>

import type { DateTimeFormat } from '../types/search-input'

const dateFormatMap: Record<DateTimeFormat, string | undefined> = {
  timestamp: 'x',
  dateObject: undefined,
  // dayjsObject: undefined,
  ISOString: '',
}

export function getElementPlusDateTimePickerValueFormat(dateFormat: DateTimeFormat) {
  return dateFormatMap[dateFormat]
}

export function getElementPlusDateTimePickerFormat(showTimePart: boolean | undefined | (() => boolean | undefined)) {
  const isShowTimePart: boolean | undefined = typeof showTimePart === 'function'
    ? showTimePart()
    : showTimePart
  if (isShowTimePart) {
    return 'YYYY-MM-DD hh:mm:ss'
  } else {
    return 'YYYY-MM-DD'
  }
}

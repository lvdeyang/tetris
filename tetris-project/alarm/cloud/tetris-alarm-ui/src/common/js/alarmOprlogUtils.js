import util from '../js/util'

export default {
  alarmLevelToString: function (alarmLevel) {
    switch (alarmLevel) {
      case 'INFO': return '信息'
      case 'MINOR': return '一般'
      case 'MAJOR': return '重要'
      case 'CRITICAL': return '严重'
    }
  },

  dateFormat: function (row, column) {
    var dateString = row[column.property]
    console.log('dataString=' + dateString)
    if (dateString === undefined) {
      return ''
    }
    console.log('dataString=' + JSON.stringify(Date.parse(dateString)))
    return util.formatDate.format(new Date(Date.parse(dateString)), 'yyyy-MM-dd hh:mm:ss')
  }
}

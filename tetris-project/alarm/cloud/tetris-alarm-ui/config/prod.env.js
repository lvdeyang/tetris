'use strict'

// 重要
const useLocalRouteConst = true

module.exports = {
  NODE_ENV: '"production"',
  USELOCALROUTE : useLocalRouteConst,
  // RESOURCE_ROOT: useLocalRouteConst? '"https://__requestIP__:8743"': '"https://__applicationIP__:8743"',
  // USER_ROOT: useLocalRouteConst? '"https://__requestIP__:8543"' :  '"https://__applicationIP__:8543"'
  ALARM_ROOT:  useLocalRouteConst? '"https://__requestIP__:9143"' : '"https://__applicationIP__:9143"',
  WEBSOCKET_URL : useLocalRouteConst? '"wss://__requestIP__:9143/"': '"wss://__applicationIP__:9143/"'
}
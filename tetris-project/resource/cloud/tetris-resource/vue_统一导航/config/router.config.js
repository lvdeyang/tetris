const user_root = document.location.origin;

const useLocalRouteConst = true

module.exports = {
  //queryRouterUrl : useLocalRouteConst?user_root + '/privilege/queryMenu':'http://192.165.56.131:8093/privilege/queryMenu',
    queryRouterUrl : useLocalRouteConst?user_root + '/privilege/queryMenu':'/privilege/queryMenu',
    serviceName :'suma-venus-resource'
};

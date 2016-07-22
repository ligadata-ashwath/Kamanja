/**
 * Created by muhammad on 7/22/16.
 */
'use strict'

angular.module('networkApp')
  .factory('serviceSocket', function () {
    var wsStatus;
    var socketUrl = 'ws://54.176.225.148:7080/v2/broker/?topics=testmessageevents_1';
    return {
      connectStatus: function (callback) {
        if ('WebSocket' in window) {

          wsStatus = new WebSocket(socketUrl);
        } else if ('MozWebSocket' in window) {

          wsStatus = new MozWebSocket(URL);
        } else {

          console.log('websocke is not supported');
          return;
        }

        wsStatus.onopen = function () {
          console.log('Open Status!');
        };

        wsStatus.onmessage = function (event) {
          callback(event.data);
        };


        wsStatus.onclose = function () {

          disconnectStatus();
          console.log('Close Status!');
        };

        wsStatus.onerror = function (event) {
          console.log('Error Status!');
        };
      },
      disconnectStatus: function () {
        if (wsStatus != null) {
          wsStatus.close();
          wsStatus = null;
        }

        console.log('Disconnect Status!');
      }
    };
  });
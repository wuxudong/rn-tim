import {
  NativeModules,
  findNodeHandle,
  NativeEventEmitter,
  NativeAppEventEmitter
} from 'react-native';

const {IMEngine} = NativeModules;
const IMEmitter = new NativeEventEmitter(IMEngine);

export default {
  ...IMEngine,
  async init(options = {}) {
    this.listener && this.listener.remove();
    let res = await IMEngine.init(options);
    console.log("init " + res)

  },
  async login(id, sig) {
    let res = await IMEngine.login(id, sig);
    console.log("login " + res)
  },
  async joinGroup(groupId) {
    let res = await IMEngine.joinGroup(groupId);
    console.log("joinGroup " + res)
  },
  async quitGroup(groupId) {
    let res = await IMEngine.quitGroup(groupId);
    console.log("quitGroup " + res)

  },
  eventEmitter(fnConf) {
    //there are no `removeListener` for NativeAppEventEmitter & DeviceEventEmitter
    this.listener && this.listener.remove();
    // this.listener = NativeAppEventEmitter.addListener('iLiveEvent', event => {
    //     fnConf[event['type']] && fnConf[event['type']](event);
    // });
    this.listener = IMEmitter.addListener(
      'im',
      (event) => {
        fnConf[event['type']] && fnConf[event['type']](event);
      }
    );
  },
  removeEmitter() {
    this.listener && this.listener.remove();
  }
};

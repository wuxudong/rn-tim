import React from 'react';
import {StyleSheet, Text, View, Button} from 'react-native';

import IMEngine from './src/IMEngine';


export default class App extends React.Component {
  constructor() {
    super()
    this.state = {
      log: ""
    }
  }

  log(s) {
    this.setState({log: this.state.log + "\n" + JSON.stringify(s)})

  }

  joinGroup(groupId) {
    IMEngine.joinGroup(groupId)
  }

  quitGroup(groupId) {
    IMEngine.quitGroup(groupId)
  }

  componentDidMount() {
    const options = {
      appid: 1400052487,
      accountType: 18808
    };

    let log = this.log.bind(this)
    IMEngine.init(options)
      .then(() =>
        IMEngine.login('wuxudong', 'eJxlj1tPgzAYhu-5FaS3GtNy2BoTL8CNiMHD3EDHTYO0YJkW0pXTjP-diUts4nv7PN-35v00TNMEm2h9keV53QpF1NgwYF6aAILzP9g0nJJMEVvSf5ANDZeMZIVicoLIdV0LQt3hlAnFC34y*nZoaS1KzdjTHZlqfl84x3vXcvBcV3g5wbtlfB3ehMJ-EOGr7708puvOSt8S9RzLwHkv2tqJs6B6wqnwxZjce3zpnSVqV826hS2HIIxQdegX3vy2T-N4y0Z2gKsgj1DWy3KzvdIqFf9gp00I4ZntYqzRjsk9r8UkWPCoWDb8CTC*jG8jEF97'))
      .then(() => {
          console.log("after init and login")
          IMEngine.eventEmitter({
            onMessage: (data) => {
              console.log(data);
              log(data)

            },
            onJoinGroup: (data) => {
              console.log(data);
              log(data)
            },
            onQuitGroup: (data) => {
              console.log(data);
              log(data)
            },
            onGroupAdd: (data) => {
              console.log(data);
              log(data)
            },
            onGroupDelete: (data) => {
              console.log(data);
              log(data)
            }
          })
        }
      )
  }

  componentWillMount() {

  }


  render() {
    return (
      <View style={styles.container}>
        <Button onPress={() => this.joinGroup("@TGS#356C4A7E4")} title="join"/>
        <Button onPress={() => this.quitGroup("@TGS#356C4A7E4")} title="quit"/>
        <Text>{this.state.log}</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});

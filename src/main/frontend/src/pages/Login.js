import React, { Component } from 'react';
import { NavigationLink } from '../components/common/NavigationLink';
import { GoogleLogin } from 'react-google-login';
import request from 'superagent';

const responseGoogle = response => {
  const idToken = response.tokenId;
  request
    .post('/signin')
    .set('Authorization', idToken)
    .set('Accept', 'application/json')
    .then(res => {
      alert('yay got ' + JSON.stringify(res.body));
    })
    .catch(err => {
      console.log(err);
    });
};

var loginButtonStyle = {
  padding: '0',
  cursor: 'pointer'
};

export default class Login extends Component {
  render() {
    return (
      <div className="align-items-center justify-content-center d-flex flex-grow-1 flex-column">
        <h1>Hello Hawaii</h1>
        <GoogleLogin
          style={loginButtonStyle}
          disabledStyle
          clientId="91011414864-oscjl6qmm6qds4kuvvh1j991rgvker3h.apps.googleusercontent.com"
          onSuccess={responseGoogle}
          onFailure={responseGoogle}
        >
          <NavigationLink>Log in</NavigationLink>
        </GoogleLogin>
      </div>
    );
  }
}

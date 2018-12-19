import React, { Component } from 'react';
import { NavigationLink } from '../components/common/navigationLink';
import { GoogleLogin } from 'react-google-login';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { requestToken } from '../store/actions/getTokenActions';
import { getAuthorization } from '../store/selectors';
import store from '../store/store';
import { push } from 'connected-react-router';
import HawaiiWallpaper from '../img/hawaii_wallpaper.jpg';
import styled, { withTheme } from 'styled-components';
import { redirect } from '../store/sagas/getTokenFromSessionStorageSaga';

const loginButtonStyle = {
  padding: '0',
  cursor: 'pointer',
  backgroundColor: '#E45052'
};

const LoginContainer = styled.div`
  background: url(${HawaiiWallpaper});
  height: 100%;
  width: 100%;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
`;

class Login extends Component {
  componentDidUpdate() {
    if (this.props.authorization) {
      store.dispatch(push('/leave'));
    }
  }

  render() {
    return (
      <LoginContainer>
        <div
          style={{
            position: 'absolute',
            width: '500px',
            height: '200px',
            backgroundColor: 'rgba(0,0,0,0.7)',
            borderRadius: '10px',
            display: 'flex',
            flexDirection: 'row',
            justifyContent: 'space-evenly',
            alignItems: 'center',
            color: 'white'
          }}
        >
          <h1>Aloha</h1>
          <GoogleLogin
            prompt="select_account"
            style={loginButtonStyle}
            disabledStyle
            clientId="91011414864-oscjl6qmm6qds4kuvvh1j991rgvker3h.apps.googleusercontent.com"
            onSuccess={this.props.requestToken}
            // TODO error handling
            // onFailure={this.props.receiveGoogleData}
          >
            <NavigationLink>Log in</NavigationLink>
          </GoogleLogin>
        </div>
      </LoginContainer>
    );
  }
}

const mapStateToProps = state => ({
  authorization: getAuthorization(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      requestToken
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Login);

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
import ExecomLogo from '../img/execom_logo.png';
import styled from 'styled-components';
import { MainLogo } from '../components/common/mainLogo';

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

const LogoContainer = styled.div`
  position: absolute;
  width: 500px;
  height: 200px;
  background-color: rgba(50, 50, 52, 0.7);
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: center;
  color: white;
  border: 2px solid rgba(50, 50, 52);
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
        <LogoContainer>
          <MainLogo>
            <p>Hawaii</p>
            <span>
              <img src={ExecomLogo} alt="execom" />
              HR tool
            </span>
          </MainLogo>
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
        </LogoContainer>
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

import { push } from 'connected-react-router';
import React, { Component } from 'react';
import { GoogleLogin } from 'react-google-login';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import styled from 'styled-components';
import { MainLogo } from '../components/common/mainLogo';
import ExecomLogo from '../img/execom_logo.png';
import HawaiiWallpaper from '../img/hawaii_wallpaper.jpg';
import { clearAuthorization } from '../store/actions/authorizationActions';
import { requestToken } from '../store/actions/getTokenActions';
import { getAuthorization } from '../store/selectors';
import store from '../store/store';

const loginButtonStyle = {
  padding: 0,
  cursor: 'pointer',
  backgroundColor: 'transparent'
};

const LoginButton = styled.span`
  color: white;
  padding: 10px 20px 10px 20px;
  background: #fa4c50;
  display: inline-block;
  font-weight: 600;
  border-radius: 10px;
`;

const LoginContainer = styled.div`
  background: url(${HawaiiWallpaper});
  height: 100%;
  width: 100%;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  flex-direction: row;
  align-items: flex-end;
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
  componentDidMount = () => {
    this.props.clearAuthorization();
    sessionStorage.clear();
  };

  componentDidUpdate() {
    if (this.props.authorization) {
      store.dispatch(push('/dashboard'));
    }
  }

  render() {
    return (
      <LoginContainer className="pb-5">
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
            disabledStyle
            style={loginButtonStyle}
            clientId="91011414864-oscjl6qmm6qds4kuvvh1j991rgvker3h.apps.googleusercontent.com"
            onSuccess={this.props.requestToken}
            icon={true}
            // TODO error handling
            // onFailure={this.props.receiveGoogleData}
          >
            <LoginButton>Log in</LoginButton>
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
      requestToken,
      clearAuthorization
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Login);

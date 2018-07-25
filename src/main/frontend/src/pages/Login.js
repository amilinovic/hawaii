import React, { Component } from 'react';
import { NavigationLink } from '../components/common/NavigationLink';
import { GoogleLogin } from 'react-google-login';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { requestToken } from '../store/actions/GetTokenActions';
import { getAuthorization } from '../store/Selectors';
import store from '../store/Store';
import { push } from 'connected-react-router';

const loginButtonStyle = {
  padding: '0',
  cursor: 'pointer'
};

class Login extends Component {
  componentDidUpdate() {
    if (this.props.authorization) {
      store.dispatch(push('/leave'));
    }
  }

  render() {
    return (
      <div className="align-items-center justify-content-center d-flex flex-grow-1 flex-column">
        <h1>Hello Hawaii</h1>
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

import React, { Component } from 'react';
import { Switch, Route } from 'react-router-dom';
import Login from './pages/Login';
import NavBar from './components/navigation/NavBar';
import { ConnectedRouter } from 'react-router-redux';
import { history } from './store/Store';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { requestAuthentication } from './store/actions/AuthenticateActions';
import { getAuthentication } from './store/Selectors';

class App extends Component {
  componentDidMount() {
    this.props.requestAuthentication();
  }

  render() {
    return (
      <ConnectedRouter history={history}>
        <Switch>
          <Route path="/login" component={Login} />
          <Route path="/" component={NavBar} />
        </Switch>
      </ConnectedRouter>
    );
  }
}

const mapStateToProps = state => ({
  authentication: getAuthentication(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      requestAuthentication
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(App);

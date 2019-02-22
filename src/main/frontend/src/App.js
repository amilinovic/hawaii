import { ConnectedRouter } from 'connected-react-router';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Route, Switch } from 'react-router-dom';
import { bindActionCreators } from 'redux';
import Login from './layout/Login';
import Panel from './layout/Panel';
import { requestTokenFromStorage } from './store/actions/getTokenFromSessionStorageActions';
import { history } from './store/store';

class App extends Component {
  componentDidMount() {
    this.props.requestTokenFromStorage();
  }

  render() {
    return (
      <ConnectedRouter history={history}>
        <Switch>
          <Route path="/login" component={Login} />
          <Route path="/" component={Panel} />
        </Switch>
      </ConnectedRouter>
    );
  }
}

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      requestTokenFromStorage
    },
    dispatch
  );

export default connect(
  null,
  mapDispatchToProps
)(App);

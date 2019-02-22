import React, { Component } from 'react';
import { Switch, Route } from 'react-router-dom';
import Login from './layout/Login';
import Panel from './layout/Panel';
import { ConnectedRouter } from 'connected-react-router';
import { history } from './store/store';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { requestTokenFromStorage } from './store/actions/getTokenFromSessionStorageActions';

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

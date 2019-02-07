import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { navigateOut } from '../../store/actions/navigateActions';

// This HOC component is used to clear out the redux state of the component where it is called when unmounting
const withResetOnNavigate = () => WrappedComponent => {
  const withResetOnNavigateClass = class withResetOnNavigateClass extends Component {
    componentWillUnmount() {
      this.props.navigateOut();
    }

    render() {
      return <WrappedComponent {...this.props} />;
    }
  };

  const mapDispatchToProps = dispatch =>
    bindActionCreators({ navigateOut }, dispatch);

  return connect(
    null,
    mapDispatchToProps
  )(withResetOnNavigateClass);
};

export default withResetOnNavigate;

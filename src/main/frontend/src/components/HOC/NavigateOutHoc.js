import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { navigateOut } from '../../store/actions/navigateActions';

// This HOC component is used to clear out the redux state of the component where it is called when unmounting
const navigateOutHoc = () => WrappedComponent => {
  const navigateOutClass = class navigateOutClass extends Component {
    componentWillUnmount() {
      this.props.navigateOut();
    }

    render() {
      return <WrappedComponent {...this.props} />;
    }
  };

  return connect(
    null,
    mapDispatchToProps
  )(navigateOutClass);
};

const mapDispatchToProps = dispatch =>
  bindActionCreators({ navigateOut }, dispatch);

export default navigateOutHoc;

import React, { Component } from 'react';

// This HOC component is used to clear out the redux state of the component where it is called when unmounting
const navigateOutHoc = () => WrappedComponent => {
  return class navigateOutHoc extends Component {
    componentWillUnmount() {
      this.props.navigateOut();
    }

    render() {
      return <WrappedComponent {...this.props} />;
    }
  };
};

export default navigateOutHoc;

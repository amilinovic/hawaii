import React, { Component } from 'react';
import { connect } from 'react-redux';
// import { bindActionCreators } from 'redux';
import { requestEmployees } from '../../store/actions/employeesActions';

class Employees extends Component {
  componentDidMount() {
    this.props.requestEmployees();
  }

  render() {
    return <div>Employees</div>;
  }
}

const mapDispatchToProps = dispatch => {
  return {
    requestEmployees: () => dispatch(requestEmployees())
  };
};

// const mapDispatchToProps = dispatch =>
//   bindActionCreators({ requestEmployees }, dispatch);

export default connect(
  null,
  mapDispatchToProps
)(Employees);

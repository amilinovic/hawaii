import React, { Component } from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';
import { bindActionCreators } from 'redux';
import { requestLeaveTypes } from '../../store/actions/leaveTypesActions';
import { getLeaveTypes } from '../../store/selectors';
import withResetOnNavigate from '../HOC/withResetOnNavigate';
import Loading from '../loading/Loading';
import LeaveTypeItem from './LeaveTypeItem';

class LeaveTypes extends Component {
  componentDidMount() {
    this.props.requestLeaveTypes();
  }

  render() {
    if (!this.props.leaveTypes) return <Loading />;

    const leaveTypesItems = this.props.leaveTypes.map(item => {
      return <LeaveTypeItem key={item.id} leaveType={item} />;
    });

    return (
      <div className="container-fluid">
        <div className="row">
          <div className="col-md-12">
            <NavLink to={'/leave-types/create'}>
              <input
                type="button"
                value="Create leave type"
                className="btn btn-primary float-right"
              />
            </NavLink>
          </div>
        </div>
        <div className="row">
          <div className="col-md-12">
            <table className="table table-hover">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Type</th>
                  <th>Subtype</th>
                  <th>Active</th>
                </tr>
              </thead>
              <tbody>{leaveTypesItems}</tbody>
            </table>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  leaveTypes: getLeaveTypes(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestLeaveTypes }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(LeaveTypes));

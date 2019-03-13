import React, { Component } from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';
import { bindActionCreators } from 'redux';
import { requestLeaveProfiles } from '../../store/actions/leaveProfilesActions';
import { getLeaveProfiles } from '../../store/selectors';
import withResetOnNavigate from '../HOC/withResetOnNavigate';
import Loading from '../loading/Loading';
import LeaveProfileItem from './LeaveProfileItem';

class LeaveProfiles extends Component {
  componentDidMount() {
    this.props.requestLeaveProfiles();
  }

  render() {
    if (!this.props.leaveProfiles) return <Loading />;

    const leaveProfiles = this.props.leaveProfiles.map(item => {
      return <LeaveProfileItem key={item.id} leaveProfile={item} />;
    });

    return (
      <div className="container-fluid">
        <div className="row">
          <div className="col-md-12">
            <NavLink to={'/leave-profiles/create'}>
              <input
                type="button"
                value="Create leave profile"
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
                </tr>
              </thead>
              <tbody>{leaveProfiles}</tbody>
            </table>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  leaveProfiles: getLeaveProfiles(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestLeaveProfiles }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(LeaveProfiles));

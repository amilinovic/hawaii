import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import Loading from '../components/loading/Loading';
import {
  requestEmployee,
  updateEmployee
} from '../store/actions/employeeActions';
import { requestLeaveProfiles } from '../store/actions/leaveProfilesActions';
import { requestTeams } from '../store/actions/teamsActions';
import { getEmployee, getLeaveProfiles, getTeams } from '../store/selectors';

const validationSchema = Yup.object().shape({
  fullName: Yup.string().required(),
  email: Yup.string()
    .email()
    .required(),
  userRole: Yup.string().required(),
  teamId: Yup.string().required(),
  jobTitle: Yup.string().required(),
  startedWorkingAtExecomDate: Yup.string().required(),
  startedWorkingDate: Yup.string().required(),
  leaveProfileId: Yup.string().required()
});

class EditEmployee extends Component {
  componentDidMount() {
    this.props.requestEmployee(this.props.match.params.id);
    this.props.requestTeams();
    this.props.requestLeaveProfiles();
  }

  render() {
    if (!this.props.employee || !this.props.teams || !this.props.leaveProfiles)
      return <Loading />;

    const teams = this.props.teams.map(team => {
      return (
        <option key={team.id} value={team.id}>
          {team.name}
        </option>
      );
    });

    const leaveProfiles = this.props.leaveProfiles.map(leaveProfile => {
      return (
        <option key={leaveProfile.id} value={leaveProfile.id}>
          {leaveProfile.name}
        </option>
      );
    });

    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Formik
          validationSchema={validationSchema}
          initialValues={this.props.employee}
          onSubmit={this.props.updateEmployee}
          enableReinitialize
        >
          {({ handleSubmit, handleChange, values, errors, touched }) => (
            <React.Fragment>
              <input
                className={`${
                  errors.fullName && touched.fullName ? 'border-danger' : ''
                } mb-3 border`}
                name="fullName"
                onChange={handleChange}
                defaultValue={values.fullName}
                placeholder="Employee name"
              />
              <input
                className={`${
                  errors.email && touched.email ? 'border-danger' : ''
                } mb-3 border`}
                name="email"
                onChange={handleChange}
                defaultValue={values.email}
                placeholder="Employee email"
              />
              <input
                className={`${
                  errors.jobTitle && touched.jobTitle ? 'border-danger' : ''
                } mb-3 border`}
                name="jobTitle"
                onChange={handleChange}
                defaultValue={values.jobTitle}
                placeholder="Employee job title"
              />
              <select
                className={`${
                  errors.userRole && touched.userRole ? 'border-danger' : ''
                } mb-3 border`}
                name="userRole"
                onChange={handleChange}
                value={values.userRole}
              >
                <option value="HR_MANAGER">HR manager</option>
                <option value="DEVELOPER">Developer</option>
              </select>
              <select
                className={`${
                  errors.leaveProfileId && touched.leaveProfileId
                    ? 'border-danger'
                    : ''
                } mb-3 border`}
                name="leaveProfileId"
                onChange={handleChange}
                value={values.leaveProfileId}
              >
                <option value="" disabled>
                  Select leave profile
                </option>
                {leaveProfiles}
              </select>
              <select
                className={`${
                  errors.teamId && touched.teamId ? 'border-danger' : ''
                } mb-3 border`}
                name="teamId"
                onChange={handleChange}
                value={values.teamId}
              >
                {teams}
              </select>
              <input
                className={`${
                  errors.startedWorkingDate && touched.startedWorkingDate
                    ? 'border-danger'
                    : ''
                } mb-3 border`}
                name="startedWorkingDate"
                onChange={handleChange}
                defaultValue={values.startedWorkingDate}
                placeholder="Started working date"
              />
              <input
                className={`${
                  errors.startedWorkingAtExecomDate &&
                  touched.startedWorkingAtExecomDate
                    ? 'border-danger'
                    : ''
                } mb-3 border`}
                name="startedWorkingAtExecomDate"
                onChange={handleChange}
                defaultValue={values.startedWorkingAtExecomDate}
                placeholder="Started working at execom date"
              />
              <button className="btn" onClick={handleSubmit} type="submit">
                Update
              </button>
            </React.Fragment>
          )}
        </Formik>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  employee: getEmployee(state),
  teams: getTeams(state),
  leaveProfiles: getLeaveProfiles(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    { updateEmployee, requestEmployee, requestTeams, requestLeaveProfiles },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(EditEmployee));

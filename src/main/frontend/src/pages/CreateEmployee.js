import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import Loading from '../components/loading/Loading';
import { createEmployee } from '../store/actions/employeeActions';
import { requestLeaveProfiles } from '../store/actions/leaveProfilesActions';
import { requestTeams } from '../store/actions/teamsActions';
import { getLeaveProfiles, getTeams } from '../store/selectors';

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

class CreateEmployee extends Component {
  componentDidMount() {
    this.props.requestTeams();
    this.props.requestLeaveProfiles();
  }

  render() {
    if (!this.props.teams) return <Loading />;

    const teams = this.props.teams.map(team => {
      return (
        <option key={team.id} value={team.id}>
          {team.name}
        </option>
      );
    });

    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Formik
          validationSchema={validationSchema}
          initialValues={{
            fullName: '',
            email: '',
            jobTitle: '',
            userRole: '',
            teamId: '',
            startedWorkingDate: '',
            startedWorkingAtExecomDate: '',
            leaveProfileId: '',
            userStatusType: 'ACTIVE'
          }}
          onSubmit={this.props.createEmployee}
        >
          {({ handleSubmit, handleChange, values, errors, touched }) => (
            <React.Fragment>
              <input
                className={`${
                  errors.fullName && touched.fullName ? 'border-danger' : ''
                } mb-3 border`}
                name="fullName"
                type="text"
                onChange={handleChange}
                placeholder="Full Name"
              />
              <input
                className={`${
                  errors.email && touched.email ? 'border-danger' : ''
                } mb-3 border`}
                name="email"
                type="text"
                onChange={handleChange}
                placeholder="Email"
              />
              <input
                className={`${
                  errors.jobTitle && touched.jobTitle ? 'border-danger' : ''
                } mb-3 border`}
                name="jobTitle"
                type="text"
                onChange={handleChange}
                placeholder="Job title"
              />
              <select
                className={`${
                  errors.userRole && touched.userRole ? 'border-danger' : ''
                } mb-3 border`}
                name="userRole"
                onChange={handleChange}
                value={values.userRole}
              >
                <option value="" disabled>
                  Select role
                </option>
                <option value="HR_MANAGER">HR manager</option>
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
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
              </select>
              <select
                className={`${
                  errors.teamId && touched.teamId ? 'border-danger' : ''
                } mb-3 border`}
                name="teamId"
                onChange={handleChange}
                value={values.teamId}
              >
                <option value="" disabled>
                  Select team
                </option>
                {teams}
              </select>
              <input
                className={`${
                  errors.startedWorkingDate && touched.startedWorkingDate
                    ? 'border-danger'
                    : ''
                } mb-3 border`}
                name="startedWorkingDate"
                type="text"
                onChange={handleChange}
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
                type="text"
                onChange={handleChange}
                placeholder="Started working at execom date"
              />
              <input
                className="mb-3 border"
                name="yearsOfService"
                type="text"
                onChange={handleChange}
                placeholder="Years of service"
              />
              <button className="btn" onClick={handleSubmit} type="submit">
                Create
              </button>
            </React.Fragment>
          )}
        </Formik>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  teams: getTeams(state),
  leaveProfiles: getLeaveProfiles(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    { createEmployee, requestTeams, requestLeaveProfiles },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(CreateEmployee));

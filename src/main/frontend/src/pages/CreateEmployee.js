import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import Loading from '../components/loading/Loading';
import { createEmployee } from '../store/actions/employeeActions';
import { requestTeams } from '../store/actions/teamsActions';
import { getTeams } from '../store/selectors';

const validationSchema = Yup.object().shape({
  fullName: Yup.string().required('Required'),
  email: Yup.string()
    .email('Invalid email')
    .required('Required'),
  userRole: Yup.string().required('Required'),
  teamId: Yup.string().required('Required'),
  jobTitle: Yup.string().required('Required'),
  startedWorkingAtExecomDate: Yup.string().required('Required'),
  startedWorkingDate: Yup.string().required('Required')
});

class CreateEmployee extends Component {
  state = {
    employee: {
      leaveProfileId: 1,
      userStatusType: 'ACTIVE'
    }
  };

  componentDidMount() {
    this.props.requestTeams();
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
            leaveProfileId: 2,
            userStatusType: 'ACTIVE'
          }}
          onSubmit={this.props.createEmployee}
        >
          {({ handleSubmit, handleChange, values, errors, touched }) => (
            <React.Fragment>
              <input
                className={`${
                  errors.fullName && touched.fullName
                    ? 'border border-danger'
                    : 'border'
                } mb-3`}
                name="fullName"
                type="text"
                onChange={handleChange}
                placeholder="Full Name"
              />
              <input
                className={`${
                  errors.email && touched.email
                    ? 'border border-danger'
                    : 'border'
                } mb-3`}
                name="email"
                type="text"
                onChange={handleChange}
                placeholder="email"
              />
              <input
                className={`${
                  errors.jobTitle && touched.jobTitle
                    ? 'border border-danger'
                    : 'border'
                } mb-3`}
                name="jobTitle"
                type="text"
                onChange={handleChange}
                placeholder="Job title"
              />
              <select
                className={`${
                  errors.userRole && touched.userRole
                    ? 'border border-danger'
                    : 'border'
                } mb-3`}
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
                  errors.teamId && touched.teamId
                    ? 'border border-danger'
                    : 'border'
                } mb-3`}
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
                    ? 'border border-danger'
                    : 'border'
                } mb-3`}
                name="startedWorkingDate"
                type="text"
                onChange={handleChange}
                placeholder="Started working date"
              />
              <input
                className={`${
                  errors.startedWorkingAtExecomDate &&
                  touched.startedWorkingAtExecomDate
                    ? 'border border-danger'
                    : 'border'
                } mb-3`}
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
  teams: getTeams(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ createEmployee, requestTeams }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(CreateEmployee));

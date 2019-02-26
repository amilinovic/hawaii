import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import SearchDropdown from '../components/search-dropdown/SearchDropdown';
import { requestEmployees } from '../store/actions/employeesActions';
import { createTeam } from '../store/actions/teamActions';
import { getSearchEmployees } from '../store/selectors';

const validationSchema = Yup.object().shape({
  name: Yup.string().required('Required')
});

class CreateTeam extends Component {
  render() {
    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Formik
          validationSchema={validationSchema}
          initialValues={{
            teamApprovers: [],
            users: []
          }}
          onSubmit={this.props.createTeam}
          render={({ handleSubmit, handleChange, values, errors, touched }) => (
            <React.Fragment>
              {console.log(touched)}
              <div className="mb-3">
                <input
                  className="w-100"
                  name="name"
                  onChange={handleChange}
                  placeholder="Team name"
                />
                {errors.name && touched.name ? (
                  <div className="text-danger">{errors.name}</div>
                ) : null}
              </div>
              <SearchDropdown results={this.props.employees} />
              <div className="d-flex justify-content-between mt-3">
                <div className="mb-5">
                  <h3>Team members</h3>
                  {!values.users.length ? (
                    <span>No members selected</span>
                  ) : (
                    values.users.map(user => {
                      return <h5 key={user.id}>{user.fullName}</h5>;
                    })
                  )}
                </div>
                <div className="mb-5">
                  <h3>Team approvers</h3>
                  {!values.teamApprovers.length ? (
                    <span>No approvers selected</span>
                  ) : (
                    values.teamApprovers.map(user => {
                      return <h5 key={user.id}>{user.fullName}</h5>;
                    })
                  )}
                </div>
              </div>
              <button className="btn" onClick={handleSubmit} type="submit">
                Create team
              </button>
            </React.Fragment>
          )}
        />
      </div>
    );
  }
}

const mapStateToProps = state => ({
  employees: getSearchEmployees(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ createTeam, requestEmployees }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(CreateTeam));

import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import { createLeaveProfile } from '../store/actions/leaveProfileActions';

const validationSchema = Yup.object().shape({
  name: Yup.string().required()
});

class CreateLeaveProfile extends Component {
  render() {
    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Formik
          validationSchema={validationSchema}
          initialValues={{
            name: ''
          }}
          onSubmit={this.props.createLeaveProfile}
          render={({
            handleSubmit,
            handleChange,
            values,
            errors,
            touched,
            setFieldValue
          }) => (
            <React.Fragment>
              <input
                className={`${
                  errors.name && touched.name ? 'border-danger' : ''
                } w-100 border mb-3`}
                name="name"
                onChange={handleChange}
                placeholder="Leave profile name"
              />

              <button className="btn" onClick={handleSubmit} type="submit">
                Create leave profile
              </button>
            </React.Fragment>
          )}
        />
      </div>
    );
  }
}

// const mapStateToProps = state => ({
//   employees: getSearchEmployees(state)
// });

const mapDispatchToProps = dispatch =>
  bindActionCreators({ createLeaveProfile }, dispatch);

export default connect(
  null,
  mapDispatchToProps
)(withResetOnNavigate()(CreateLeaveProfile));

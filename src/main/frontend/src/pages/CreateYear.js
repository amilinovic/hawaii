import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Switch from 'react-switch';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import { createYear } from '../store/actions/yearActions';

const validationSchema = Yup.object().shape({
  year: Yup.string().required()
});

class CreateYear extends Component {
  render() {
    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Formik
          validationSchema={validationSchema}
          initialValues={{
            year: '',
            active: false,
            allowances: []
          }}
          onSubmit={this.props.createYear}
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
                  errors.year && touched.year ? 'border-danger' : ''
                } w-100 mb-3 border`}
                name="year"
                defaultValue={values.year}
                placeholder="Year"
                onChange={handleChange}
              />
              <div className="d-flex justify-content-between mb-3">
                <h5>Active</h5>
                <Switch
                  onChange={() => setFieldValue('active', !values.active)}
                  checked={values.active}
                  name="active"
                />
              </div>
              <button className="btn" onClick={handleSubmit} type="submit">
                Create year
              </button>
            </React.Fragment>
          )}
        />
      </div>
    );
  }
}

const mapDispatchToProps = dispatch =>
  bindActionCreators({ createYear }, dispatch);

export default connect(
  null,
  mapDispatchToProps
)(withResetOnNavigate()(CreateYear));

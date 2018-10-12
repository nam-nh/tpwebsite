import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Category from './category';
import ItemGroup from './item-group';
import ItemSubGroup from './item-sub-group';
import Item from './item';
import Service from './service';
import News from './news';
import Project from './project';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/category`} component={Category} />
      <ErrorBoundaryRoute path={`${match.url}/item-group`} component={ItemGroup} />
      <ErrorBoundaryRoute path={`${match.url}/item-sub-group`} component={ItemSubGroup} />
      <ErrorBoundaryRoute path={`${match.url}/item`} component={Item} />
      <ErrorBoundaryRoute path={`${match.url}/service`} component={Service} />
      <ErrorBoundaryRoute path={`${match.url}/news`} component={News} />
      <ErrorBoundaryRoute path={`${match.url}/project`} component={Project} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;

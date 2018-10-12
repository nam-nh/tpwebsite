import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import News from './news';
import NewsDetail from './news-detail';
import NewsUpdate from './news-update';
import NewsDeleteDialog from './news-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={NewsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={NewsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={NewsDetail} />
      <ErrorBoundaryRoute path={match.url} component={News} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={NewsDeleteDialog} />
  </>
);

export default Routes;

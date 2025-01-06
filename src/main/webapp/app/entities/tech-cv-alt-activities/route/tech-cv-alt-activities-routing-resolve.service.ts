import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITechCVAltActivities } from '../tech-cv-alt-activities.model';
import { TechCVAltActivitiesService } from '../service/tech-cv-alt-activities.service';

const techCVAltActivitiesResolve = (route: ActivatedRouteSnapshot): Observable<null | ITechCVAltActivities> => {
  const id = route.params['id'];
  if (id) {
    return inject(TechCVAltActivitiesService)
      .find(id)
      .pipe(
        mergeMap((techCVAltActivities: HttpResponse<ITechCVAltActivities>) => {
          if (techCVAltActivities.body) {
            return of(techCVAltActivities.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default techCVAltActivitiesResolve;

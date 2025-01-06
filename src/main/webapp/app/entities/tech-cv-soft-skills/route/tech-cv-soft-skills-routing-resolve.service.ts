import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITechCVSoftSkills } from '../tech-cv-soft-skills.model';
import { TechCVSoftSkillsService } from '../service/tech-cv-soft-skills.service';

const techCVSoftSkillsResolve = (route: ActivatedRouteSnapshot): Observable<null | ITechCVSoftSkills> => {
  const id = route.params['id'];
  if (id) {
    return inject(TechCVSoftSkillsService)
      .find(id)
      .pipe(
        mergeMap((techCVSoftSkills: HttpResponse<ITechCVSoftSkills>) => {
          if (techCVSoftSkills.body) {
            return of(techCVSoftSkills.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default techCVSoftSkillsResolve;

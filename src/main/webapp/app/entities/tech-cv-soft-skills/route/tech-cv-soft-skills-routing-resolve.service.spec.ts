import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { ITechCVSoftSkills } from '../tech-cv-soft-skills.model';
import { TechCVSoftSkillsService } from '../service/tech-cv-soft-skills.service';

import techCVSoftSkillsResolve from './tech-cv-soft-skills-routing-resolve.service';

describe('TechCVSoftSkills routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: TechCVSoftSkillsService;
  let resultTechCVSoftSkills: ITechCVSoftSkills | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(TechCVSoftSkillsService);
    resultTechCVSoftSkills = undefined;
  });

  describe('resolve', () => {
    it('should return ITechCVSoftSkills returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        techCVSoftSkillsResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTechCVSoftSkills = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTechCVSoftSkills).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        techCVSoftSkillsResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTechCVSoftSkills = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTechCVSoftSkills).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ITechCVSoftSkills>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        techCVSoftSkillsResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTechCVSoftSkills = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTechCVSoftSkills).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});

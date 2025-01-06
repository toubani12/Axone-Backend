import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IRecruiter } from 'app/entities/recruiter/recruiter.model';
import { RecruiterService } from 'app/entities/recruiter/service/recruiter.service';
import { ICandidate } from 'app/entities/candidate/candidate.model';
import { CandidateService } from 'app/entities/candidate/service/candidate.service';
import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { IEmployer } from 'app/entities/employer/employer.model';
import { EmployerService } from 'app/entities/employer/service/employer.service';
import { IDomain } from '../domain.model';
import { DomainService } from '../service/domain.service';
import { DomainFormService } from './domain-form.service';

import { DomainUpdateComponent } from './domain-update.component';

describe('Domain Management Update Component', () => {
  let comp: DomainUpdateComponent;
  let fixture: ComponentFixture<DomainUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let domainFormService: DomainFormService;
  let domainService: DomainService;
  let recruiterService: RecruiterService;
  let candidateService: CandidateService;
  let applicationService: ApplicationService;
  let employerService: EmployerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, DomainUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DomainUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DomainUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    domainFormService = TestBed.inject(DomainFormService);
    domainService = TestBed.inject(DomainService);
    recruiterService = TestBed.inject(RecruiterService);
    candidateService = TestBed.inject(CandidateService);
    applicationService = TestBed.inject(ApplicationService);
    employerService = TestBed.inject(EmployerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Recruiter query and add missing value', () => {
      const domain: IDomain = { id: 456 };
      const recruiters: IRecruiter[] = [{ id: 13734 }];
      domain.recruiters = recruiters;

      const recruiterCollection: IRecruiter[] = [{ id: 24856 }];
      jest.spyOn(recruiterService, 'query').mockReturnValue(of(new HttpResponse({ body: recruiterCollection })));
      const additionalRecruiters = [...recruiters];
      const expectedCollection: IRecruiter[] = [...additionalRecruiters, ...recruiterCollection];
      jest.spyOn(recruiterService, 'addRecruiterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ domain });
      comp.ngOnInit();

      expect(recruiterService.query).toHaveBeenCalled();
      expect(recruiterService.addRecruiterToCollectionIfMissing).toHaveBeenCalledWith(
        recruiterCollection,
        ...additionalRecruiters.map(expect.objectContaining),
      );
      expect(comp.recruitersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Candidate query and add missing value', () => {
      const domain: IDomain = { id: 456 };
      const candidates: ICandidate[] = [{ id: 2656 }];
      domain.candidates = candidates;

      const candidateCollection: ICandidate[] = [{ id: 12326 }];
      jest.spyOn(candidateService, 'query').mockReturnValue(of(new HttpResponse({ body: candidateCollection })));
      const additionalCandidates = [...candidates];
      const expectedCollection: ICandidate[] = [...additionalCandidates, ...candidateCollection];
      jest.spyOn(candidateService, 'addCandidateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ domain });
      comp.ngOnInit();

      expect(candidateService.query).toHaveBeenCalled();
      expect(candidateService.addCandidateToCollectionIfMissing).toHaveBeenCalledWith(
        candidateCollection,
        ...additionalCandidates.map(expect.objectContaining),
      );
      expect(comp.candidatesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Application query and add missing value', () => {
      const domain: IDomain = { id: 456 };
      const applications: IApplication[] = [{ id: 27802 }];
      domain.applications = applications;

      const applicationCollection: IApplication[] = [{ id: 15878 }];
      jest.spyOn(applicationService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationCollection })));
      const additionalApplications = [...applications];
      const expectedCollection: IApplication[] = [...additionalApplications, ...applicationCollection];
      jest.spyOn(applicationService, 'addApplicationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ domain });
      comp.ngOnInit();

      expect(applicationService.query).toHaveBeenCalled();
      expect(applicationService.addApplicationToCollectionIfMissing).toHaveBeenCalledWith(
        applicationCollection,
        ...additionalApplications.map(expect.objectContaining),
      );
      expect(comp.applicationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employer query and add missing value', () => {
      const domain: IDomain = { id: 456 };
      const employer: IEmployer = { id: 25016 };
      domain.employer = employer;

      const employerCollection: IEmployer[] = [{ id: 11469 }];
      jest.spyOn(employerService, 'query').mockReturnValue(of(new HttpResponse({ body: employerCollection })));
      const additionalEmployers = [employer];
      const expectedCollection: IEmployer[] = [...additionalEmployers, ...employerCollection];
      jest.spyOn(employerService, 'addEmployerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ domain });
      comp.ngOnInit();

      expect(employerService.query).toHaveBeenCalled();
      expect(employerService.addEmployerToCollectionIfMissing).toHaveBeenCalledWith(
        employerCollection,
        ...additionalEmployers.map(expect.objectContaining),
      );
      expect(comp.employersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const domain: IDomain = { id: 456 };
      const recruiter: IRecruiter = { id: 31828 };
      domain.recruiters = [recruiter];
      const candidate: ICandidate = { id: 25653 };
      domain.candidates = [candidate];
      const application: IApplication = { id: 15788 };
      domain.applications = [application];
      const employer: IEmployer = { id: 23367 };
      domain.employer = employer;

      activatedRoute.data = of({ domain });
      comp.ngOnInit();

      expect(comp.recruitersSharedCollection).toContain(recruiter);
      expect(comp.candidatesSharedCollection).toContain(candidate);
      expect(comp.applicationsSharedCollection).toContain(application);
      expect(comp.employersSharedCollection).toContain(employer);
      expect(comp.domain).toEqual(domain);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDomain>>();
      const domain = { id: 123 };
      jest.spyOn(domainFormService, 'getDomain').mockReturnValue(domain);
      jest.spyOn(domainService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domain });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: domain }));
      saveSubject.complete();

      // THEN
      expect(domainFormService.getDomain).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(domainService.update).toHaveBeenCalledWith(expect.objectContaining(domain));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDomain>>();
      const domain = { id: 123 };
      jest.spyOn(domainFormService, 'getDomain').mockReturnValue({ id: null });
      jest.spyOn(domainService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domain: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: domain }));
      saveSubject.complete();

      // THEN
      expect(domainFormService.getDomain).toHaveBeenCalled();
      expect(domainService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDomain>>();
      const domain = { id: 123 };
      jest.spyOn(domainService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domain });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(domainService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRecruiter', () => {
      it('Should forward to recruiterService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(recruiterService, 'compareRecruiter');
        comp.compareRecruiter(entity, entity2);
        expect(recruiterService.compareRecruiter).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCandidate', () => {
      it('Should forward to candidateService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(candidateService, 'compareCandidate');
        comp.compareCandidate(entity, entity2);
        expect(candidateService.compareCandidate).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareApplication', () => {
      it('Should forward to applicationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(applicationService, 'compareApplication');
        comp.compareApplication(entity, entity2);
        expect(applicationService.compareApplication).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEmployer', () => {
      it('Should forward to employerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employerService, 'compareEmployer');
        comp.compareEmployer(entity, entity2);
        expect(employerService.compareEmployer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

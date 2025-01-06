import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IEmployer } from 'app/entities/employer/employer.model';
import { EmployerService } from 'app/entities/employer/service/employer.service';
import { IRecruiter } from 'app/entities/recruiter/recruiter.model';
import { RecruiterService } from 'app/entities/recruiter/service/recruiter.service';
import { ICandidate } from 'app/entities/candidate/candidate.model';
import { CandidateService } from 'app/entities/candidate/service/candidate.service';
import { INDA } from '../nda.model';
import { NDAService } from '../service/nda.service';
import { NDAFormService } from './nda-form.service';

import { NDAUpdateComponent } from './nda-update.component';

describe('NDA Management Update Component', () => {
  let comp: NDAUpdateComponent;
  let fixture: ComponentFixture<NDAUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let nDAFormService: NDAFormService;
  let nDAService: NDAService;
  let employerService: EmployerService;
  let recruiterService: RecruiterService;
  let candidateService: CandidateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NDAUpdateComponent],
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
      .overrideTemplate(NDAUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NDAUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    nDAFormService = TestBed.inject(NDAFormService);
    nDAService = TestBed.inject(NDAService);
    employerService = TestBed.inject(EmployerService);
    recruiterService = TestBed.inject(RecruiterService);
    candidateService = TestBed.inject(CandidateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employer query and add missing value', () => {
      const nDA: INDA = { id: 456 };
      const employer: IEmployer = { id: 1655 };
      nDA.employer = employer;

      const employerCollection: IEmployer[] = [{ id: 19815 }];
      jest.spyOn(employerService, 'query').mockReturnValue(of(new HttpResponse({ body: employerCollection })));
      const additionalEmployers = [employer];
      const expectedCollection: IEmployer[] = [...additionalEmployers, ...employerCollection];
      jest.spyOn(employerService, 'addEmployerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ nDA });
      comp.ngOnInit();

      expect(employerService.query).toHaveBeenCalled();
      expect(employerService.addEmployerToCollectionIfMissing).toHaveBeenCalledWith(
        employerCollection,
        ...additionalEmployers.map(expect.objectContaining),
      );
      expect(comp.employersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Recruiter query and add missing value', () => {
      const nDA: INDA = { id: 456 };
      const mediator: IRecruiter = { id: 11818 };
      nDA.mediator = mediator;

      const recruiterCollection: IRecruiter[] = [{ id: 24082 }];
      jest.spyOn(recruiterService, 'query').mockReturnValue(of(new HttpResponse({ body: recruiterCollection })));
      const additionalRecruiters = [mediator];
      const expectedCollection: IRecruiter[] = [...additionalRecruiters, ...recruiterCollection];
      jest.spyOn(recruiterService, 'addRecruiterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ nDA });
      comp.ngOnInit();

      expect(recruiterService.query).toHaveBeenCalled();
      expect(recruiterService.addRecruiterToCollectionIfMissing).toHaveBeenCalledWith(
        recruiterCollection,
        ...additionalRecruiters.map(expect.objectContaining),
      );
      expect(comp.recruitersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Candidate query and add missing value', () => {
      const nDA: INDA = { id: 456 };
      const candidate: ICandidate = { id: 23464 };
      nDA.candidate = candidate;

      const candidateCollection: ICandidate[] = [{ id: 5532 }];
      jest.spyOn(candidateService, 'query').mockReturnValue(of(new HttpResponse({ body: candidateCollection })));
      const additionalCandidates = [candidate];
      const expectedCollection: ICandidate[] = [...additionalCandidates, ...candidateCollection];
      jest.spyOn(candidateService, 'addCandidateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ nDA });
      comp.ngOnInit();

      expect(candidateService.query).toHaveBeenCalled();
      expect(candidateService.addCandidateToCollectionIfMissing).toHaveBeenCalledWith(
        candidateCollection,
        ...additionalCandidates.map(expect.objectContaining),
      );
      expect(comp.candidatesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const nDA: INDA = { id: 456 };
      const employer: IEmployer = { id: 8998 };
      nDA.employer = employer;
      const mediator: IRecruiter = { id: 26672 };
      nDA.mediator = mediator;
      const candidate: ICandidate = { id: 27080 };
      nDA.candidate = candidate;

      activatedRoute.data = of({ nDA });
      comp.ngOnInit();

      expect(comp.employersSharedCollection).toContain(employer);
      expect(comp.recruitersSharedCollection).toContain(mediator);
      expect(comp.candidatesSharedCollection).toContain(candidate);
      expect(comp.nDA).toEqual(nDA);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INDA>>();
      const nDA = { id: 123 };
      jest.spyOn(nDAFormService, 'getNDA').mockReturnValue(nDA);
      jest.spyOn(nDAService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nDA });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nDA }));
      saveSubject.complete();

      // THEN
      expect(nDAFormService.getNDA).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(nDAService.update).toHaveBeenCalledWith(expect.objectContaining(nDA));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INDA>>();
      const nDA = { id: 123 };
      jest.spyOn(nDAFormService, 'getNDA').mockReturnValue({ id: null });
      jest.spyOn(nDAService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nDA: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nDA }));
      saveSubject.complete();

      // THEN
      expect(nDAFormService.getNDA).toHaveBeenCalled();
      expect(nDAService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INDA>>();
      const nDA = { id: 123 };
      jest.spyOn(nDAService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nDA });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(nDAService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployer', () => {
      it('Should forward to employerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employerService, 'compareEmployer');
        comp.compareEmployer(entity, entity2);
        expect(employerService.compareEmployer).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
  });
});

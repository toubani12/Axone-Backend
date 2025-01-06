import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { IDomain } from 'app/entities/domain/domain.model';
import { DomainService } from 'app/entities/domain/service/domain.service';
import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { ICandidate } from '../candidate.model';
import { CandidateService } from '../service/candidate.service';
import { CandidateFormService } from './candidate-form.service';

import { CandidateUpdateComponent } from './candidate-update.component';

describe('Candidate Management Update Component', () => {
  let comp: CandidateUpdateComponent;
  let fixture: ComponentFixture<CandidateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let candidateFormService: CandidateFormService;
  let candidateService: CandidateService;
  let technicalCVService: TechnicalCVService;
  let domainService: DomainService;
  let applicationService: ApplicationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, CandidateUpdateComponent],
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
      .overrideTemplate(CandidateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CandidateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    candidateFormService = TestBed.inject(CandidateFormService);
    candidateService = TestBed.inject(CandidateService);
    technicalCVService = TestBed.inject(TechnicalCVService);
    domainService = TestBed.inject(DomainService);
    applicationService = TestBed.inject(ApplicationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call techCV query and add missing value', () => {
      const candidate: ICandidate = { id: 456 };
      const techCV: ITechnicalCV = { id: 14060 };
      candidate.techCV = techCV;

      const techCVCollection: ITechnicalCV[] = [{ id: 12177 }];
      jest.spyOn(technicalCVService, 'query').mockReturnValue(of(new HttpResponse({ body: techCVCollection })));
      const expectedCollection: ITechnicalCV[] = [techCV, ...techCVCollection];
      jest.spyOn(technicalCVService, 'addTechnicalCVToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidate });
      comp.ngOnInit();

      expect(technicalCVService.query).toHaveBeenCalled();
      expect(technicalCVService.addTechnicalCVToCollectionIfMissing).toHaveBeenCalledWith(techCVCollection, techCV);
      expect(comp.techCVSCollection).toEqual(expectedCollection);
    });

    it('Should call Domain query and add missing value', () => {
      const candidate: ICandidate = { id: 456 };
      const domains: IDomain[] = [{ id: 31894 }];
      candidate.domains = domains;

      const domainCollection: IDomain[] = [{ id: 18564 }];
      jest.spyOn(domainService, 'query').mockReturnValue(of(new HttpResponse({ body: domainCollection })));
      const additionalDomains = [...domains];
      const expectedCollection: IDomain[] = [...additionalDomains, ...domainCollection];
      jest.spyOn(domainService, 'addDomainToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidate });
      comp.ngOnInit();

      expect(domainService.query).toHaveBeenCalled();
      expect(domainService.addDomainToCollectionIfMissing).toHaveBeenCalledWith(
        domainCollection,
        ...additionalDomains.map(expect.objectContaining),
      );
      expect(comp.domainsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Application query and add missing value', () => {
      const candidate: ICandidate = { id: 456 };
      const applications: IApplication[] = [{ id: 3745 }];
      candidate.applications = applications;

      const applicationCollection: IApplication[] = [{ id: 16123 }];
      jest.spyOn(applicationService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationCollection })));
      const additionalApplications = [...applications];
      const expectedCollection: IApplication[] = [...additionalApplications, ...applicationCollection];
      jest.spyOn(applicationService, 'addApplicationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidate });
      comp.ngOnInit();

      expect(applicationService.query).toHaveBeenCalled();
      expect(applicationService.addApplicationToCollectionIfMissing).toHaveBeenCalledWith(
        applicationCollection,
        ...additionalApplications.map(expect.objectContaining),
      );
      expect(comp.applicationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const candidate: ICandidate = { id: 456 };
      const techCV: ITechnicalCV = { id: 11949 };
      candidate.techCV = techCV;
      const domain: IDomain = { id: 21155 };
      candidate.domains = [domain];
      const applications: IApplication = { id: 1304 };
      candidate.applications = [applications];

      activatedRoute.data = of({ candidate });
      comp.ngOnInit();

      expect(comp.techCVSCollection).toContain(techCV);
      expect(comp.domainsSharedCollection).toContain(domain);
      expect(comp.applicationsSharedCollection).toContain(applications);
      expect(comp.candidate).toEqual(candidate);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICandidate>>();
      const candidate = { id: 123 };
      jest.spyOn(candidateFormService, 'getCandidate').mockReturnValue(candidate);
      jest.spyOn(candidateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidate }));
      saveSubject.complete();

      // THEN
      expect(candidateFormService.getCandidate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(candidateService.update).toHaveBeenCalledWith(expect.objectContaining(candidate));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICandidate>>();
      const candidate = { id: 123 };
      jest.spyOn(candidateFormService, 'getCandidate').mockReturnValue({ id: null });
      jest.spyOn(candidateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidate: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidate }));
      saveSubject.complete();

      // THEN
      expect(candidateFormService.getCandidate).toHaveBeenCalled();
      expect(candidateService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICandidate>>();
      const candidate = { id: 123 };
      jest.spyOn(candidateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(candidateService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTechnicalCV', () => {
      it('Should forward to technicalCVService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(technicalCVService, 'compareTechnicalCV');
        comp.compareTechnicalCV(entity, entity2);
        expect(technicalCVService.compareTechnicalCV).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDomain', () => {
      it('Should forward to domainService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(domainService, 'compareDomain');
        comp.compareDomain(entity, entity2);
        expect(domainService.compareDomain).toHaveBeenCalledWith(entity, entity2);
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
  });
});

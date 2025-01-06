import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IEmployer } from 'app/entities/employer/employer.model';
import { EmployerService } from 'app/entities/employer/service/employer.service';
import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { ITemplate } from '../template.model';
import { TemplateService } from '../service/template.service';
import { TemplateFormService } from './template-form.service';

import { TemplateUpdateComponent } from './template-update.component';

describe('Template Management Update Component', () => {
  let comp: TemplateUpdateComponent;
  let fixture: ComponentFixture<TemplateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let templateFormService: TemplateFormService;
  let templateService: TemplateService;
  let employerService: EmployerService;
  let applicationService: ApplicationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TemplateUpdateComponent],
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
      .overrideTemplate(TemplateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TemplateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    templateFormService = TestBed.inject(TemplateFormService);
    templateService = TestBed.inject(TemplateService);
    employerService = TestBed.inject(EmployerService);
    applicationService = TestBed.inject(ApplicationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employer query and add missing value', () => {
      const template: ITemplate = { id: 456 };
      const owner: IEmployer = { id: 30380 };
      template.owner = owner;

      const employerCollection: IEmployer[] = [{ id: 4516 }];
      jest.spyOn(employerService, 'query').mockReturnValue(of(new HttpResponse({ body: employerCollection })));
      const additionalEmployers = [owner];
      const expectedCollection: IEmployer[] = [...additionalEmployers, ...employerCollection];
      jest.spyOn(employerService, 'addEmployerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ template });
      comp.ngOnInit();

      expect(employerService.query).toHaveBeenCalled();
      expect(employerService.addEmployerToCollectionIfMissing).toHaveBeenCalledWith(
        employerCollection,
        ...additionalEmployers.map(expect.objectContaining),
      );
      expect(comp.employersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Application query and add missing value', () => {
      const template: ITemplate = { id: 456 };
      const applications: IApplication[] = [{ id: 14351 }];
      template.applications = applications;

      const applicationCollection: IApplication[] = [{ id: 13290 }];
      jest.spyOn(applicationService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationCollection })));
      const additionalApplications = [...applications];
      const expectedCollection: IApplication[] = [...additionalApplications, ...applicationCollection];
      jest.spyOn(applicationService, 'addApplicationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ template });
      comp.ngOnInit();

      expect(applicationService.query).toHaveBeenCalled();
      expect(applicationService.addApplicationToCollectionIfMissing).toHaveBeenCalledWith(
        applicationCollection,
        ...additionalApplications.map(expect.objectContaining),
      );
      expect(comp.applicationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const template: ITemplate = { id: 456 };
      const owner: IEmployer = { id: 8742 };
      template.owner = owner;
      const application: IApplication = { id: 2813 };
      template.applications = [application];

      activatedRoute.data = of({ template });
      comp.ngOnInit();

      expect(comp.employersSharedCollection).toContain(owner);
      expect(comp.applicationsSharedCollection).toContain(application);
      expect(comp.template).toEqual(template);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITemplate>>();
      const template = { id: 123 };
      jest.spyOn(templateFormService, 'getTemplate').mockReturnValue(template);
      jest.spyOn(templateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ template });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: template }));
      saveSubject.complete();

      // THEN
      expect(templateFormService.getTemplate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(templateService.update).toHaveBeenCalledWith(expect.objectContaining(template));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITemplate>>();
      const template = { id: 123 };
      jest.spyOn(templateFormService, 'getTemplate').mockReturnValue({ id: null });
      jest.spyOn(templateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ template: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: template }));
      saveSubject.complete();

      // THEN
      expect(templateFormService.getTemplate).toHaveBeenCalled();
      expect(templateService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITemplate>>();
      const template = { id: 123 };
      jest.spyOn(templateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ template });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(templateService.update).toHaveBeenCalled();
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

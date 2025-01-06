import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IAppAccount } from 'app/entities/app-account/app-account.model';
import { AppAccountService } from 'app/entities/app-account/service/app-account.service';
import { AppAccountTypeService } from '../service/app-account-type.service';
import { IAppAccountType } from '../app-account-type.model';
import { AppAccountTypeFormService } from './app-account-type-form.service';

import { AppAccountTypeUpdateComponent } from './app-account-type-update.component';

describe('AppAccountType Management Update Component', () => {
  let comp: AppAccountTypeUpdateComponent;
  let fixture: ComponentFixture<AppAccountTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let appAccountTypeFormService: AppAccountTypeFormService;
  let appAccountTypeService: AppAccountTypeService;
  let appAccountService: AppAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AppAccountTypeUpdateComponent],
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
      .overrideTemplate(AppAccountTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppAccountTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    appAccountTypeFormService = TestBed.inject(AppAccountTypeFormService);
    appAccountTypeService = TestBed.inject(AppAccountTypeService);
    appAccountService = TestBed.inject(AppAccountService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AppAccount query and add missing value', () => {
      const appAccountType: IAppAccountType = { id: 456 };
      const appAccounts: IAppAccount[] = [{ id: 9480 }];
      appAccountType.appAccounts = appAccounts;

      const appAccountCollection: IAppAccount[] = [{ id: 2188 }];
      jest.spyOn(appAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: appAccountCollection })));
      const additionalAppAccounts = [...appAccounts];
      const expectedCollection: IAppAccount[] = [...additionalAppAccounts, ...appAccountCollection];
      jest.spyOn(appAccountService, 'addAppAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appAccountType });
      comp.ngOnInit();

      expect(appAccountService.query).toHaveBeenCalled();
      expect(appAccountService.addAppAccountToCollectionIfMissing).toHaveBeenCalledWith(
        appAccountCollection,
        ...additionalAppAccounts.map(expect.objectContaining),
      );
      expect(comp.appAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const appAccountType: IAppAccountType = { id: 456 };
      const appAccount: IAppAccount = { id: 5611 };
      appAccountType.appAccounts = [appAccount];

      activatedRoute.data = of({ appAccountType });
      comp.ngOnInit();

      expect(comp.appAccountsSharedCollection).toContain(appAccount);
      expect(comp.appAccountType).toEqual(appAccountType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppAccountType>>();
      const appAccountType = { id: 123 };
      jest.spyOn(appAccountTypeFormService, 'getAppAccountType').mockReturnValue(appAccountType);
      jest.spyOn(appAccountTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appAccountType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appAccountType }));
      saveSubject.complete();

      // THEN
      expect(appAccountTypeFormService.getAppAccountType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(appAccountTypeService.update).toHaveBeenCalledWith(expect.objectContaining(appAccountType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppAccountType>>();
      const appAccountType = { id: 123 };
      jest.spyOn(appAccountTypeFormService, 'getAppAccountType').mockReturnValue({ id: null });
      jest.spyOn(appAccountTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appAccountType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appAccountType }));
      saveSubject.complete();

      // THEN
      expect(appAccountTypeFormService.getAppAccountType).toHaveBeenCalled();
      expect(appAccountTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppAccountType>>();
      const appAccountType = { id: 123 };
      jest.spyOn(appAccountTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appAccountType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(appAccountTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAppAccount', () => {
      it('Should forward to appAccountService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(appAccountService, 'compareAppAccount');
        comp.compareAppAccount(entity, entity2);
        expect(appAccountService.compareAppAccount).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

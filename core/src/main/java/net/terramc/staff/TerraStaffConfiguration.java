package net.terramc.staff;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownEntryTranslationPrefix;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.terramc.staff.data.AddonData;

@SuppressWarnings("FieldMayBeFinal")
@ConfigName("settings")
@SpriteTexture("sprite/settings")
public class TerraStaffConfiguration extends AddonConfig {

  @SettingSection(value = "general", center = true)

  @SpriteSlot(x = 0)
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);



  @SettingSection(value = "staff", center = true)

  @IntroducedIn(namespace = "terramc_staff", value = "1.0.0")
  @SpriteSlot(x = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> hideOwnTag = new ConfigProperty<>(false);



  @SettingSection(value = "admin", center = true)

  @IntroducedIn(namespace = "terramc_staff", value = "1.0.0")
  @DropdownSetting
  @DropdownEntryTranslationPrefix("terramc_staff.settings.cloudNotifyType.type")
  private final ConfigProperty<AddonData.CloudNotifyType> cloudNotifyType = new ConfigProperty<>(AddonData.CloudNotifyType.CHAT);

  @IntroducedIn(namespace = "terramc_staff", value = "1.0.0")
  @SpriteSlot(x = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> showTagAlways = new ConfigProperty<>(false);



  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> hideOwnTag() {
    return hideOwnTag;
  }

  public ConfigProperty<AddonData.CloudNotifyType> cloudNotifyType() {
    return cloudNotifyType;
  }

  public ConfigProperty<Boolean> showTagAlways() {
    return showTagAlways;
  }

}

package miragefairy2019.colormaker.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class ParsingTextField<T> extends JTextField
{

	public ParsingTextField()
	{
		super();
		init();
	}

	public ParsingTextField(Document doc, String text, int columns)
	{
		super(doc, text, columns);
		init();
	}

	public ParsingTextField(int columns)
	{
		super(columns);
		init();
	}

	public ParsingTextField(String text, int columns)
	{
		super(text, columns);
		init();
	}

	public ParsingTextField(String text)
	{
		super(text);
		init();
	}

	private void init()
	{

		// イベン登録
		{

			// アクションリスナー登録
			addActionListener(e -> parse());

			// ドキュメントのリスナーに常にドキュメントリスナーを配置する
			{
				DocumentListener dl = new DocumentListener() {
					@Override
					public void removeUpdate(DocumentEvent e)
					{
						parse();
					}

					@Override
					public void insertUpdate(DocumentEvent e)
					{
						parse();
					}

					@Override
					public void changedUpdate(DocumentEvent e)
					{
						parse();
					}
				};
				if (getDocument() != null) {
					getDocument().addDocumentListener(dl);
				}
				addPropertyChangeListener("document", e -> {
					if (e.getOldValue() instanceof Document) {
						((Document) e.getOldValue()).removeDocumentListener(dl);
					}
					if (e.getNewValue() instanceof Document) {
						((Document) e.getNewValue()).addDocumentListener(dl);
					}
				});
			}

		}

	}

	//

	public Color colorSuccess = Color.decode("#BFFFBF");
	public Color colorError = Color.decode("#FFBFBF");
	public Function<String, Optional<T>> parser;
	public ArrayList<Consumer<T>> listener = new ArrayList<>();

	private void parse()
	{
		Optional<T> res = parser.apply(getText());
		if (res.isPresent()) {
			value = res.get();
			listener.forEach(l -> {
				try {
					l.accept(value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			setBackground(colorSuccess);
		} else {
			setBackground(colorError);
		}
	}

	//

	public Function<T, String> builder;

	private T value;

	public T getValue()
	{
		return value;
	}

	public void setValue(T value)
	{
		setText(builder.apply(value));
	}

}
